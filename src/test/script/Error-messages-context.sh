#!/bin/sh

for cas_de_test in src/test/deca/context/invalid/*.deca
do
    test_context "$cas_de_test"
done
