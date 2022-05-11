#!/bin/sh

test_synt_invalide () {
    # $1 = premier argument.
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec attendu pour test_synt sur $1."
    else
        echo "Succes inattendu de test_synt sur $1."
        exit 1
    fi
}

for cas_de_test in src/test/deca/syntax/invalid/*.deca
do
    test_synt_invalide "$cas_de_test"
    echo ""
done
