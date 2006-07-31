#
# If you fancy doing cross compilation on macosx instead, here are some links.
# I couldn't manage to get that working, though.
#
#
#
# http://wiki.wxwidgets.org/wiki.pl?Cross-Compiling_Under_Linux
# http://wiki.wxwidgets.org/wiki.pl?Building_Win32_Apps_On_Linux
#
# arg 1 : host
# arg 2 : username
# arg 3 : remote path
# arg 4 : libraryname
# arg 5 : output file
# arg 6 : local ico dir

host=$1
user=$2
remotepath=$3
libname=$4
outfile=$5
icodir=$6
sources=/Users/greg/dev/projects/smileylibmaker/smileylibmaker/src/scripts/miranda/dll*
curdir=`dirname $0`
expectScript=$curdir/miranda_build.macosx.expect
outfileName=`basename $outfile`
#./miranda_build.macosx.expect

#scp dll headers and stuff
scp $sources $user@$host:$remotepath
#scp ico and generated desc files
scp $icodir/*.ico $icodir/../*.msl $icodir/../*.rc $user@$host:$remotepath

#delegate to build script using expect
expect $expectScript $host $user $remotepath $libname $outfileName

echo 'yoohoo'
#get the file back from remote host
scp $user@$host:$remotepath/$outfileName $outfile
