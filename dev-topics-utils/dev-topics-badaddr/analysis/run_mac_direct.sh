#!/usr/bin/env bash
##
# run_mac_direct.sh - execute all tests
##
echo ""
echo "Running compiled class files directly"
cmp_class_path=target/classes
cmp_main_class=don.demo.AdditionChecker
cmd="java -cp .:${cmp_class_path} ${cmp_main_class}"
echo " "
echo "c: ${cmd}"
echo " "
${cmd}
echo " "
echo "Done"
echo " "