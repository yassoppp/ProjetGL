#!/bin/sh

test_decompile () {
    #decompile1 = $1-1.deca
    #decompile2 = $1-2.deca
    decac -p "$1" > decompile1
    decac -p decompile1 > decompile2
    #cat decompile1
    #cat decompile2
    if [ $(diff decompile1 decompile2)]
    then
        echo "Echec inattendu de decompile sur $1"
        exit 1
    else
        echo "Succes attendu de decompile sur $1"
    fi
}

for cas_de_test in src/test/deca/context/invalid/*.deca
do
    test_decompile "$cas_de_test"
    echo ""
done

for cas_de_test in src/test/deca/context/valid/*.deca
do
    test_decompile "$cas_de_test"
    echo ""
done

for cas_de_test in src/test/deca/syntax/valid/*.deca
do
    test_decompile "$cas_de_test"
    echo ""
done

for cas_de_test in src/test/deca/codegen/valid/provided/*.deca
do
    test_decompile "$cas_de_test"
    echo ""
done
rm decompile1
rm decompile2
