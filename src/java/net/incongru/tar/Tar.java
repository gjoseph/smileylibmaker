/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package net.incongru.tar;

import org.apache.tools.tar.TarConstants;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarOutputStream;
import org.apache.tools.zip.UnixStat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is copied from the Ant taskdef and just adapted to be more generic and simpler.
 * Still depends on ant for now.
 */
public class Tar {

    private File tarFile;
    private File baseDir;
    private TarFileSet fileset;
    private TarLongFileMode longFileMode = TarLongFileMode.WARN;
    private TarCompressionMethod compression;
    private String prefix;

    //File sourceDir,
    public Tar(File tarFile, File sourceDir, String prefix, TarCompressionMethod compressionType) {
        this.tarFile = tarFile;
        this.baseDir = sourceDir;
        this.prefix = prefix;
        this.fileset = new TarFileSet(sourceDir);
        this.compression = compressionType;
    }


    /**
     * Indicates whether the user has been warned about long files already.
     */
    private boolean longWarningGiven = false;

    /**
     * Set how to handle long files, those with a path&gt;100 chars.
     * Optional, default=warn.
     * <p/>
     * Allowable values are
     * <ul>
     * <li>  truncate - paths are truncated to the maximum length
     * <li>  fail - paths greater than the maximum cause a build exception
     * <li>  warn - paths greater than the maximum cause a warning and GNU is used
     * <li>  gnu - GNU extensions are used for any paths greater than the maximum.
     * <li>  omit - paths greater than the maximum are omitted from the archive
     * </ul>
     *
     * @param mode the mode to handle long file names.
     */
    public void setLongfile(TarLongFileMode mode) {
        this.longFileMode = mode;
    }

    /**
     * Set compression method.
     * Allowable values are
     * <ul>
     * <li>  none - no compression
     * <li>  gzip - Gzip compression
     * <li>  bzip2 - Bzip2 compression
     * </ul>
     *
     * @param mode the compression method.
     */
    public void setCompression(TarCompressionMethod mode) {
        this.compression = mode;
    }

    public void execute() throws IOException {
        if (tarFile == null) {
            throw new IllegalStateException("tarfile attribute must be set!");
        }

        if (tarFile.exists() && tarFile.isDirectory()) {
            throw new IOException("tarfile is a directory!");
        }

        if (tarFile.exists() && !tarFile.canWrite()) {
            throw new IOException("Can not write to the specified tarfile!");
        }

        if (baseDir != null) {
            if (!baseDir.exists()) {
                throw new IOException("basedir does not exist!");
            }
        }

        System.out.println("Building tar: " + tarFile.getAbsolutePath());

        TarOutputStream tOut = null;
        try {
            tOut = new TarOutputStream(compression.compress(new BufferedOutputStream(new FileOutputStream(tarFile))));
            tOut.setDebug(true);
            if ((TarLongFileMode.TRUNCATE.equals(longFileMode))) {
                tOut.setLongFileMode(TarOutputStream.LONGFILE_TRUNCATE);
            } else if ((TarLongFileMode.FAIL.equals(longFileMode))
                    || (TarLongFileMode.OMIT.equals(longFileMode))) {
                tOut.setLongFileMode(TarOutputStream.LONGFILE_ERROR);
            } else {
                // warn or GNU
                tOut.setLongFileMode(TarOutputStream.LONGFILE_GNU);
            }

            longWarningGiven = false;
            String[] files = fileset.getFiles();
            if (files.length > 1 && fileset.getFullpath().length() > 0) {
                throw new IllegalStateException("fullpath attribute may only be specified for filesets that specify a single file.");
            }
            for (int i = 0; i < files.length; i++) {
                //File f = new File(baseDir.getParent(), files[i]);
                //File f = new File(baseDir, files[i]);
                File f = new File(files[i]);
                String name = files[i].replace(File.separatorChar, '/');
                if (name.startsWith(prefix)) {
                    name = name.substring(prefix.length());
                }
                tarFile(f, tOut, name, fileset);
            }
        } finally {
            if (tOut != null) {
                try {
                    // close up
                    tOut.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * tar a file
     *
     * @param file       the file to tar
     * @param tOut       the output stream
     * @param vPath      the path name of the file to tar
     * @param tarFileSet the fileset that the file came from.
     * @throws java.io.IOException on error
     */
    protected void tarFile(File file, TarOutputStream tOut, String vPath, TarFileSet tarFileSet) throws IOException {
        FileInputStream fIn = null;

        String fullpath = tarFileSet.getFullpath();
        if (fullpath.length() > 0) {
            vPath = fullpath;
        } else {
            // don't add "" to the archive
            if (vPath.length() <= 0) {
                return;
            }

            if (file.isDirectory() && !vPath.endsWith("/")) {
                vPath += "/";
            }

            String prefix = tarFileSet.getPrefix();
            // '/' is appended for compatibility with the zip task.
            if (prefix.length() > 0 && !prefix.endsWith("/")) {
                prefix = prefix + "/";
            }
            vPath = prefix + vPath;
        }

        if (vPath.startsWith("/") && !tarFileSet.getPreserveLeadingSlashes()) {
            int l = vPath.length();
            if (l <= 1) {
                // we would end up adding "" to the archive
                return;
            }
            vPath = vPath.substring(1, l);
        }

        try {
            if (vPath.length() >= TarConstants.NAMELEN) {
                // TODO : this logic should be in the modes themselves
                if (TarLongFileMode.OMIT.equals(longFileMode)) {
                    System.out.println("Omitting: " + vPath);
                    return;
                } else if ((TarLongFileMode.WARN.equals(longFileMode))) {
                    System.out.println("Entry: " + vPath + " longer than " + TarConstants.NAMELEN + " characters.");
                    if (!longWarningGiven) {
                        System.out.println("Resulting tar file can only be processed successfully by GNU compatible tar commands");
                        longWarningGiven = true;
                    }
                } else if ((TarLongFileMode.FAIL.equals(longFileMode))) {
                    throw new IOException("Entry: " + vPath + " longer than " + TarConstants.NAMELEN + "characters. (TarLongFileMode is in 'fail' mode)");
                }
            }

            TarEntry te = new TarEntry(vPath);
            te.setModTime(file.lastModified());
            if (!file.isDirectory()) {
                te.setSize(file.length());
                te.setMode(tarFileSet.getMode());
            } else {
                te.setMode(tarFileSet.getDirMode());
            }
            te.setUserName(tarFileSet.getUserName());
            te.setGroupName(tarFileSet.getGroup());
            te.setUserId(tarFileSet.getUid());
            te.setGroupId(tarFileSet.getGid());

            tOut.putNextEntry(te);

            if (!file.isDirectory()) {
                fIn = new FileInputStream(file);

                byte[] buffer = new byte[8 * 1024];
                int count = 0;
                do {
                    tOut.write(buffer, 0, count);
                    count = fIn.read(buffer, 0, buffer.length);
                } while (count != -1);
            }

            tOut.closeEntry();
        } finally {
            if (fIn != null) {
                fIn.close();
            }
        }
    }

    /**
     * This is a FileSet with the option to specify permissions
     * and other attributes.
     */
    public static class TarFileSet {//extends FileSet {
        private String[] files = null;

        private int fileMode = UnixStat.FILE_FLAG | UnixStat.DEFAULT_FILE_PERM;
        private int dirMode = UnixStat.DIR_FLAG | UnixStat.DEFAULT_DIR_PERM;

        private String userName = "";
        private String groupName = "";
        private int uid;
        private int gid;
        private String prefix = "";
        private String fullpath = "";
        private boolean preserveLeadingSlashes = false;

        private File fsBasedir;

        /**
         * Creates a new <code>TarFileSet</code> instance.
         * Using a fileset as a constructor argument.
         *
         * @param fileset a <code>FileSet</code> value
         */
        //public TarFileSet(FileSet fileset) {
        //  super(fileset);
        //}

        /**
         * Creates a new <code>TarFileSet</code> instance.
         */
        public TarFileSet(File basedir) {
            this.fsBasedir = basedir;
        }

        /**
         * Get a list of files and directories specified in the fileset.
         *
         * @return a list of file and directory names, relative to
         *         the baseDir for the project.
         */
        public String[] getFiles() {
            if (files == null) {
                ArrayList list = new ArrayList();
                list.add(fsBasedir.getPath());
                listDir(list, fsBasedir);
                files = (String[]) list.toArray(new String[list.size()]);
                Arrays.sort(files);
            }

            return files;
        }

        private void listDir(List list, File dir) {
            File[] childFiles = dir.listFiles();
            for (int i = 0; i < childFiles.length; i++) {
                File file = childFiles[i];
                list.add(file.getPath());
                if (file.isDirectory()) {
                    listDir(list, file);
                }
            }
        }

        /**
         * A 3 digit octal string, specify the user, group and
         * other modes in the standard Unix fashion;
         * optional, default=0644
         *
         * @param octalString a 3 digit octal string.
         */
        public void setMode(String octalString) {
            this.fileMode = UnixStat.FILE_FLAG | Integer.parseInt(octalString, 8);
        }

        /**
         * @return the current mode.
         */
        public int getMode() {
            return fileMode;
        }

        /**
         * A 3 digit octal string, specify the user, group and
         * other modes in the standard Unix fashion;
         * optional, default=0755
         *
         * @param octalString a 3 digit octal string.
         * @since Ant 1.6
         */
        public void setDirMode(String octalString) {
            this.dirMode = UnixStat.DIR_FLAG | Integer.parseInt(octalString, 8);
        }

        /**
         * @return the current directory mode
         * @since Ant 1.6
         */
        public int getDirMode() {
            return dirMode;
        }

        /**
         * The username for the tar entry
         * This is not the same as the UID.
         *
         * @param userName the user name for the tar entry.
         */
        public void setUserName(String userName) {
            this.userName = userName;
        }

        /**
         * @return the user name for the tar entry
         */
        public String getUserName() {
            return userName;
        }

        /**
         * The uid for the tar entry
         * This is not the same as the User name.
         *
         * @param uid the id of the user for the tar entry.
         */
        public void setUid(int uid) {
            this.uid = uid;
        }

        /**
         * @return the uid for the tar entry
         */
        public int getUid() {
            return uid;
        }

        /**
         * The groupname for the tar entry; optional, default=""
         * This is not the same as the GID.
         *
         * @param groupName the group name string.
         */
        public void setGroup(String groupName) {
            this.groupName = groupName;
        }

        /**
         * @return the group name string.
         */
        public String getGroup() {
            return groupName;
        }

        /**
         * The GID for the tar entry; optional, default="0"
         * This is not the same as the group name.
         *
         * @param gid the group id.
         */
        public void setGid(int gid) {
            this.gid = gid;
        }

        /**
         * @return the group identifier.
         */
        public int getGid() {
            return gid;
        }

        /**
         * If the prefix attribute is set, all files in the fileset
         * are prefixed with that path in the archive.
         * optional.
         *
         * @param prefix the path prefix.
         */
        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        /**
         * @return the path prefix for the files in the fileset.
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * If the fullpath attribute is set, the file in the fileset
         * is written with that path in the archive. The prefix attribute,
         * if specified, is ignored. It is an error to have more than one file specified in
         * such a fileset.
         *
         * @param fullpath the path to use for the file in a fileset.
         */
        public void setFullpath(String fullpath) {
            this.fullpath = fullpath;
        }

        /**
         * @return the path to use for a single file fileset.
         */
        public String getFullpath() {
            return fullpath;
        }

        /**
         * Flag to indicates whether leading `/'s should
         * be preserved in the file names.
         * Optional, default is <code>false</code>.
         *
         * @param b the leading slashes flag.
         */
        public void setPreserveLeadingSlashes(boolean b) {
            this.preserveLeadingSlashes = b;
        }

        /**
         * @return the leading slashes flag.
         */
        public boolean getPreserveLeadingSlashes() {
            return preserveLeadingSlashes;
        }
    }
}
