# run_all.sh
#
# Run the three test classes with timing
#
# Invocation: ./run_all.sh 1> run_all_mac.lst 2>&1
#

echo
echo Run all three concurrency levels
echo
xx_cp=./Concurrent.jar
export JAVA_OPTIONS="-Xmx512M -Xms512M"

echo
echo === SequentialRunner from $xx_cp using $JAVA_OPTIONS
java -cp .:$xx_cp don.demo.concurrent.SequentialRunner
ret=$?
if [ $ret -ne 0 ]; then
  echo
  echo "SequentialRunner failed with [$ret]"
  exit $ret
fi

echo
echo === ConcurrentRunner from $xx_cp using $JAVA_OPTIONS
java -cp .:$xx_cp don.demo.concurrent.ConcurrentRunner
ret=$?
if [ $ret -ne 0 ]; then
  echo
  echo "ConcurrentRunner failed with [$ret]"
  exit $ret
fi

echo
echo === HighlyConcurrentRunner from $xx_cp using $JAVA_OPTIONS
java -cp .:$xx_cp don.demo.concurrent.HighlyConcurrentRunner
ret=$?
if [ $ret -ne 0 ]; then
  echo
  echo "HighlyConcurrentRunner failed with [$ret]"
  exit $ret
fi

echo
echo Successfully Completed.
