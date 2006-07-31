:: argument 1 : libraryname
:: argument 2 : build dir
:: argument 3 : sources dir (dllmain.c)
@echo off
:1
c:\apps\mingw\bin\gcc.exe -c %3\dllmain.c -o %2\dllmain.o  -DBUILDING_DLL=1
IF %ERRORLEVEL% GTR 0 GOTO End

:2
c:\apps\mingw\bin\windres.exe -i %2\%1.rc -I rc -o %2\%1.res -O coff
IF %ERRORLEVEL% GTR 0 GOTO End

:3
c:\apps\mingw\bin\dllwrap.exe --output-def lib%1.def --implib %2\lib%1.a %2\dllmain.o %2\%1.res -L"c:\apps\mingw\lib" --no-export-all-symbols --add-stdcall-alias -o %2\%1.dll

:End
ECHO errlevel = %ERRORLEVEL%
