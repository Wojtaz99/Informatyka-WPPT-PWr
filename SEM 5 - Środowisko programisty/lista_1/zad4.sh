#!/bin/bash

IFS=$'\n'
Pliki=$(find $1 -type f)
WszystkieSlowa=$(for Plik in $Pliki; do
        Slowa=$(tr '[A-Z] ' '[a-z]\n' < $Plik) 
        (for Slowo in $Slowa; 
        do
            echo $Slowo
        done) | sort | uniq
    done | sort | uniq)
for Slowo in $WszystkieSlowa; do
    echo $Slowo
    grep -H -i -w "$Slowo" $Pliki
done
