import os
import unittest

from helpers.extraction import count_bounded_tokens, find_bounded_tokens

data_rel_location = '../resources/plugins_data.txt'


class ExtractTokenMainCase(unittest.TestCase):
    def test_main_key(self):
        cur_dir_name = os.path.dirname(os.path.abspath(__file__))
        data_location = os.path.abspath(os.path.join(cur_dir_name, data_rel_location))
        info = count_bounded_tokens(data_location, '<artifactId>',
                                    '</artifactId>')
        self.assertIsNotNone(info)
        main_ky = '__MAIN__'
        self.assertTrue(main_ky in info)
        self.assertEqual(data_location + '|<artifactId>|</artifactId>', info[main_ky])


class ExtractTokenCountCase(unittest.TestCase):
    def test__key_counts(self):
        cur_dir_name = os.path.dirname(os.path.abspath(__file__))
        data_location = os.path.abspath(os.path.join(cur_dir_name, data_rel_location))
        info = count_bounded_tokens(data_location, '<artifactId>',
                                    '</artifactId>')
        self.assertIsNotNone(info)
        key_count = len(info)
        self.assertEqual(9, key_count, "wrong key count")
        key = 'maven-shade-plugin'
        self.assertTrue(key in info, " no " + key + "key present")
        self.assertEqual(13, info[key], "count for " + key + " INCORRECT")
        key = 'maven-jar-plugin'
        self.assertTrue(key in info, " no " + key + "key present")
        self.assertEqual(1, info[key], "count for " + key + " INCORRECT")


class ListTokensMainCase(unittest.TestCase):
    def test_main_key(self):
        cur_dir_name = os.path.dirname(os.path.abspath(__file__))
        data_location = os.path.abspath(os.path.join(cur_dir_name, data_rel_location))
        info = find_bounded_tokens(data_location, '<artifactId>',
                                   '</artifactId>')
        self.assertIsNotNone(info)
        main_ky = '__MAIN__'
        self.assertTrue(main_ky in info)
        self.assertEqual([data_location + '|<artifactId>|</artifactId>'], info[main_ky])


class ListTokensRefCase(unittest.TestCase):
    def test_key_refs(self):
        cur_dir_name = os.path.dirname(os.path.abspath(__file__))
        data_location = os.path.abspath(os.path.join(cur_dir_name, data_rel_location))
        info = find_bounded_tokens(data_location, '<artifactId>',
                                   '</artifactId>')
        self.assertIsNotNone(info)
        main_ky = '__MAIN__'
        self.assertTrue(main_ky in info)
        self.assertEqual([data_location + '|<artifactId>|</artifactId>'], info[main_ky])


##
# {'__MAIN__':
#     ['D:\\GitHub\\DemoDev\\StaticResources\\helpers\\src\\unittest\\resources\\plugins_data.txt|<artifactId>|</artifactId>'],
# 'maven-resources-plugin': {'dev-topics-category-optimization\\pom.xml', 'pom.xml',
#     'dev-topics-generationutils\\pom.xml',
#     'dev-topics-devops\\dev-topics-dependencies\\pom.xml'},
# 'maven-compiler-plugin': {'dev-topics-category-optimization\\pom.xml', 'pom.xml',
#     'dev-topics-generationutils\\pom.xml',
#     'dev-topics-devops\\dev-topics-dependencies\\pom.xml'},
# 'maven-shade-plugin': {'dev-topics-memoryindexing\\dev-topics-simple-linearsearch\\pom.xml',
#     'dev-topics-generationutils\\pom.xml', 'dev-topics-nosql\\dev-topics-cachewrappeddb\\pom.xml',
#     'dev-topics-codingexams\\dev-topics-lumendata-duplicatechecker\\pom.xml',
#     'dev-topics-codingexams\\dev-topics-amazon-invoiceparser\\pom.xml',
#     'dev-topics-codingexams\\dev-topics-cruise-control\\pom.xml',
#     'dev-topics-codingexams\\dev-topics-cisco-sieve\\pom.xml',
#     'dev-topics-codingexams\\dev-topics-elance-diagonal\\pom.xml',
#     'dev-topics-codingexams\\dev-topics-gesail-twowriters\\pom.xml',
#     'dev-topics-codingexams\\dev-topics-amazon-rangeconsolidator\\pom.xml',
#     'dev-topics-codingexams\\dev-topics-amazon-codingchallange\\pom.xml',
#     'dev-topics-devops\\dev-topics-dependencies\\pom.xml',
#     'dev-topics-memoryindexing\\dev-topics-topx-query\\pom.xml'},
# 'maven-war-plugin': {'dev-topics-jerseyservices\\pom.xml'},
# 'maven-jar-plugin': {'pom.xml'},
# 'maven-surefire-plugin': {'pom.xml'}
##


if __name__ == '__main__':
    unittest.main()
