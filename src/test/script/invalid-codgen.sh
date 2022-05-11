#!/bin/sh

test_codgen_invalide () {
    # $1 = premier argument.
    if decac "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec attendu de decac sur $1."
    else
        echo "Succes inattendu de decac sur $1."
        exit 1
    fi
}

for cas_de_test in src/test/deca/codegen/invalid/provided/*.deca
do
    test_codgen_invalide "$cas_de_test"
done
