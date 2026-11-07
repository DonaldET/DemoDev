import click
import requests

@click.command()
@click.option('--keyword', prompt='Enter the keyword to search for', help='The keyword to search for books.')
@click.option('--max_pages', default=5, help='Maximum number of pages to fetch.')
@click.option('--output_file', default='books.html', help='Output file name.')
def get_books_and_save_as_html(keyword, max_pages, output_file):
    url = "https://vivzzxfqg1-1.algolianet.com/1/indexes/*/queries"
    headers = {
        "content-type": "application/json",
        "x-algolia-api-key": "7e71e45d51bc6b98154d9f9aeb389202",
        "x-algolia-application-id": "VIVZZXFQG1"
    }
    unique_books = {}
    page = 0

    while page < max_pages:
        payload = {
            "requests": [
                {
                    "indexName": "subs_search_new_prod",
                    "params": f"facetFilters=%5B%5B%22released%3AEarly%20Access%22%2C%22released%3AAvailable%22%5D%5D&facets=%5B%22product_type%22%2C%22released%22%2C%22published_year%22%2C%22category%22%2C%22imprint%22%2C%22concept%22%2C%22primary_tool%22%2C%22primary_language%22%5D&filters=product_type%3A%22Book%22%20OR%20product_type%3A%22Book%20%2B%20AI%20Assistant%22%20OR%20product_type%3A%22Project%22%20OR%20product_type%3A%22Audiobook%22%20OR%20product_type%3A%22exam%20trainer%22%20OR%20product_type%3A%22Learning%20Path%22%20OR%20product_type%3A%22Expert%20Reading%20Lists%22&highlightPostTag=%3C%2Fais-highlight-0000000000%3E&highlightPreTag=%3Cais-highlight-0000000000%3E&hitsPerPage=12&maxValuesPerFacet=20&page={page}&query={keyword}&tagFilters="
                }
            ]
        }

        response = requests.post(url, json=payload, headers=headers)
        response.raise_for_status()

        results = response.json().get('results', [])
        if not results:
            break

        hits = results[0].get('hits', [])
        if not hits:
            break

        for book in hits:
            title = book.get('title')
            seo_url = book.get('seoUrl')
            if title and seo_url:
                full_url = f"https://www.packtpub.com{seo_url}"
                # Use the title as the key in the dictionary to avoid duplicates
                if title not in unique_books:
                    unique_books[title] = full_url

        page += 1

    # Save to HTML with links
    with open(output_file, 'w', encoding='utf-8') as file:
        file.write('<html><body><table>\n')
        file.write('<tr><th>Title</th><th>URL</th></tr>\n')
        for title, url in unique_books.items():
            file.write(f'<tr><td>{title}</td><td><a href="{url}" target="_blank">{url}</a></td></tr>\n')
        file.write('</table></body></html>')

    # Print the total number of unique books found
    click.echo(f"Total unique books found: {len(unique_books)}")

if __name__ == '__main__':
    get_books_and_save_as_html()