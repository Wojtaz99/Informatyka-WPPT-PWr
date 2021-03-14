#!/bin/bash

(for File in $(svn ls -r$1 -R $2 | grep '[^\/]$'); do
    filename=$2$File
    plik=$(svn cat -r$1 $filename)
    Slowa=$(echo $plik | tr '[A-Z] ' '[a-z]\n') 
    (for Slowo in $Slowa; 
    do
        echo $Slowo
    done) | sort | uniq
done) | sort | uniq -c

