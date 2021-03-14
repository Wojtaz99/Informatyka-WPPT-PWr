#!/bin/bash

IFS=$'\n'
for Plik in $(find $1 -type f); do
    cat $Plik | while read linia; do
		Powtorzenia=$(echo $linia | tr ' ' '\n' | grep -w -v -H '^$' | sort | uniq -d)
		if [ ! -z "$Powtorzenia" ]; then
			echo "$Plik $linia" 
		fi
	done
done
