:: run_python.cmd
: Run python algorithms
@echo off
echo. 
echo. Python Algorithm Testing

echo. 
echo. 
echo. Initial Raw Algorithm from Article
python ..\kmer_algo\kmer_raw.py

python ..\kmer_algo\kmer_raw_fix.py

echo. 
echo. 
echo. Cleaned up Odometer Algorithm

python ..\kmer_algo\kmer_article.py

python ..\kmer_algo\kmer_article_odometer.py

python ..\kmer_algo\kmer_article_c.py

