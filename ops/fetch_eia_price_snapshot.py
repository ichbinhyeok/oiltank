"""Bootstrap a reviewed public EIA snapshot; production refresh uses the Java parser."""
import datetime
import json
import pathlib
import re
import requests
from bs4 import BeautifulSoup

root = pathlib.Path(__file__).resolve().parent.parent
url = 'https://www.eia.gov/dnav/pet/PET_PRI_WFR_A_EPD2F_PRS_DPGAL_W.htm'
response = requests.get(url, timeout=30)
response.raise_for_status()
html = '\n'.join(line.rstrip() for line in response.text.replace('\r', '').expandtabs(4).split('\n')).rstrip() + '\n'
soup = BeautifulSoup(html, 'html.parser')
dates = [datetime.datetime.strptime(x.get_text(strip=True), '%m/%d/%y').date().isoformat() for x in soup.select('th.Series5')]
history = [dict(date=date, prices={}) for date in dates]
aliases = {'U.S.': 'us', 'East Coast (PADD 1)': 'east-coast', 'New England (PADD 1A)': 'new-england'}
states = 'Connecticut,Maine,Massachusetts,New Hampshire,Rhode Island,Vermont,Delaware,Maryland,New Jersey,New York,Pennsylvania,North Carolina,Virginia,Indiana,Iowa,Kentucky,Michigan,Minnesota,Nebraska,Ohio,Wisconsin'.split(',')
aliases.update({name: name.lower().replace(' ', '-') for name in states})
for row in soup.select('tr.DataRow'):
    label = row.select_one('td.DataStub1,td.DataStub2')
    if label is None or label.get_text(strip=True) not in aliases:
        continue
    prices = [x.get_text(strip=True) for x in row.select('td.DataB,td.Current2')]
    assert len(prices) == len(history)
    for observation, price in zip(history, prices):
        observation['prices'][aliases[label.get_text(strip=True)]] = float(price)
assert len(history) >= 2 and all(len(x['prices']) == 24 for x in history)
def field(label):
    value = re.search(label + r'\s*(\d{1,2}/\d{1,2}/\d{4})', html, re.I).group(1)
    return datetime.datetime.strptime(value, '%m/%d/%Y').date().isoformat()
data = dict(releasedOn=field(r'(?<!Next )Release Date:'), nextRelease=field('Next Release Date:'),
            checkedOn=datetime.datetime.now(datetime.timezone(datetime.timedelta(hours=9))).date().isoformat(), history=history)
(root / 'src/main/resources/data/normalized/heating-oil-prices.json').write_text(json.dumps(data, indent=2) + '\n', encoding='utf-8')
fixtures = root / 'src/test/resources'
fixtures.mkdir(exist_ok=True)
(fixtures / ('eia-residential-' + data['releasedOn'] + '.html')).write_text(html, encoding='utf-8', newline='\n')
print(json.dumps(dict(observed=dates, release=data['releasedOn'], next=data['nextRelease'], areas=24)))
