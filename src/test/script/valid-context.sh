#!/bin/sh

test_context_valide () {
    # $1 = premier argument.
    if test_context "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec inattendu pour test_context sur $1."
        exit 1
    else
        echo "Succes attendu de test_context sur $1."
    fi
}


for cas_de_test in src/test/deca/context/valid/*.deca
do
    test_context_valide "$cas_de_test"
    echo ""
done
