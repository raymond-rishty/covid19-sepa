sam package --profile rrishty --template-file sam.yaml --s3-bucket lambda-artifacts.rrishty --output-template-file packaged.yaml
sam deploy --profile rrishty --region us-east-1 --template-file ./packaged.yaml --stack-name covid-19-regional-pa-report --capabilities CAPABILITY_IAM
pandoc <(cat web/southeast.md web/footer.md) -c pandoc.css -t html5 -o web/southeast.html
pandoc <(cat web/southcentral.md web/footer.md) -c pandoc.css -t html5 -o web/southcentral.html
pandoc <(cat web/southwest.md web/footer.md) -c pandoc.css -t html5 -o web/southwest.html
pandoc <(cat web/northeast.md web/footer.md) -c pandoc.css -t html5 -o web/northeast.html
pandoc <(cat web/northcentral.md web/footer.md) -c pandoc.css -t html5 -o web/northcentral.html
pandoc <(cat web/northwest.md web/footer.md) -c pandoc.css -t html5 -o web/northwest.html
pandoc <(cat web/consolidated.md web/footer.md) -c pandoc.css -t html5 -o web/consolidated.html
pandoc <(cat web/chestercounty.md web/footer.md) -c pandoc.css -t html5 -o web/chestercounty.html
pandoc <(cat web/centrecounty.md web/footer.md) -c pandoc.css -t html5 -o web/centrecounty.html
pandoc <(cat web/albemarlecountyva.md web/footer.md) -c pandoc.css -t html5 -o web/albemarlecountyva.html
pandoc <(cat web/charlottesvilleva.md web/footer.md) -c pandoc.css -t html5 -o web/charlottesvilleva.html

aws s3 cp --profile rrishty --acl public-read web/southeast.html s3://covid19.data.rrishty/southeast.html
aws s3 cp --profile rrishty --acl public-read web/southcentral.html s3://covid19.data.rrishty/southcentral.html
aws s3 cp --profile rrishty --acl public-read web/southwest.html s3://covid19.data.rrishty/southwest.html
aws s3 cp --profile rrishty --acl public-read web/northeast.html s3://covid19.data.rrishty/northeast.html
aws s3 cp --profile rrishty --acl public-read web/northcentral.html s3://covid19.data.rrishty/northcentral.html
aws s3 cp --profile rrishty --acl public-read web/northwest.html s3://covid19.data.rrishty/northwest.html
aws s3 cp --profile rrishty --acl public-read web/index.html s3://covid19.data.rrishty/index.html
aws s3 cp --profile rrishty --acl public-read web/consolidated.html s3://covid19.data.rrishty/consolidated.html
aws s3 cp --profile rrishty --acl public-read web/chestercounty.html s3://covid19.data.rrishty/chestercounty.html
aws s3 cp --profile rrishty --acl public-read web/centrecounty.html s3://covid19.data.rrishty/centrecounty.html
aws s3 cp --profile rrishty --acl public-read web/albemarlecountyva.html s3://covid19.data.rrishty/albemarlecountyva.html
aws s3 cp --profile rrishty --acl public-read web/charlottesvilleva.html s3://covid19.data.rrishty/charlottesvilleva.html
aws s3 cp --profile rrishty --acl public-read web/cumberlandcounty.html s3://covid19.data.rrishty/cumberlandcounty.html
aws s3 cp --profile rrishty --acl public-read web/bexarcountytx.html s3://covid19.data.rrishty/bexarcountytx.html
aws s3 cp --profile rrishty --acl public-read web/pandoc.css s3://covid19.data.rrishty/pandoc.css

aws s3 cp --profile rrishty --acl public-read web/southeast.html s3://pacovid19.rishty.net/southeast.html
aws s3 cp --profile rrishty --acl public-read web/southcentral.html s3://pacovid19.rishty.net/southcentral.html
aws s3 cp --profile rrishty --acl public-read web/southwest.html s3://pacovid19.rishty.net/southwest.html
aws s3 cp --profile rrishty --acl public-read web/northeast.html s3://pacovid19.rishty.net/northeast.html
aws s3 cp --profile rrishty --acl public-read web/northcentral.html s3://pacovid19.rishty.net/northcentral.html
aws s3 cp --profile rrishty --acl public-read web/northwest.html s3://pacovid19.rishty.net/northwest.html
aws s3 cp --profile rrishty --acl public-read web/index.html s3://pacovid19.rishty.net/index.html
aws s3 cp --profile rrishty --acl public-read web/consolidated.html s3://pacovid19.rishty.net/consolidated.html
aws s3 cp --profile rrishty --acl public-read web/chestercounty.html s3://pacovid19.rishty.net/chestercounty.html
aws s3 cp --profile rrishty --acl public-read web/centrecounty.html s3://pacovid19.rishty.net/centrecounty.html
aws s3 cp --profile rrishty --acl public-read web/albemarlecountyva.html s3://pacovid19.rishty.net/albemarlecountyva.html
aws s3 cp --profile rrishty --acl public-read web/charlottesvilleva.html s3://pacovid19.rishty.net/charlottesvilleva.html
aws s3 cp --profile rrishty --acl public-read web/cumberlandcounty.html s3://pacovid19.rishty.net/cumberlandcounty.html
aws s3 cp --profile rrishty --acl public-read web/bexarcountytx.html s3://pacovid19.rishty.net/bexarcountytx.html
aws s3 cp --profile rrishty --acl public-read web/pandoc.css s3://pacovid19.rishty.net/pandoc.css

aws s3 cp --profile rrishty --acl public-read web/redirect.html s3://covid19.data.rrishty/report.html
