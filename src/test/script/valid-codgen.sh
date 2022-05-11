#!/bin/sh

test_codgen_valide () {
    # $1 = premier argument.
    if decac "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec inattendu pour decac sur $1."
        exit 1
    else
        echo "Succes attendu de decac sur $1."
    fi
}


for cas_de_test in src/test/deca/codegen/valid/provided/*.deca
do
    test_codgen_valide "$cas_de_test"
    echo ""
done
