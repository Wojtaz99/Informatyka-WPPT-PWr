#!/bin/bash

for Plik in $(find $1 -type f);
do
    sed -i 's/a/A/g' $Plik
done
