sam package --profile rrishty --template-file sam.yaml --s3-bucket lambda-artifacts.rrishty --output-template-file packaged.yaml
sam deploy --profile rrishty --region us-east-1 --template-file ./packaged.yaml --stack-name covid-19-regional-pa-report --capabilities CAPABILITY_IAM
pandoc web/southeast.md -c pandoc.css -o web/southeast.html
pandoc web/southcentral.md -c pandoc.css -o web/southcentral.html
pandoc web/southwest.md -c pandoc.css -o web/southwest.html
pandoc web/northeast.md -c pandoc.css -o web/northeast.html
pandoc web/northcentral.md -c pandoc.css -o web/northcentral.html
pandoc web/northwest.md -c pandoc.css -o web/northwest.html
aws s3 cp --profile rrishty --acl public-read web/southeast.html s3://covid19.data.rrishty/southeast.html
aws s3 cp --profile rrishty --acl public-read web/southcentral.html s3://covid19.data.rrishty/southcentral.html
aws s3 cp --profile rrishty --acl public-read web/southwest.html s3://covid19.data.rrishty/southwest.html
aws s3 cp --profile rrishty --acl public-read web/northeast.html s3://covid19.data.rrishty/northeast.html
aws s3 cp --profile rrishty --acl public-read web/northcentral.html s3://covid19.data.rrishty/northcentral.html
aws s3 cp --profile rrishty --acl public-read web/northwest.html s3://covid19.data.rrishty/northwest.html
aws s3 cp --profile rrishty --acl public-read web/pandoc.css s3://covid19.data.rrishty/pandoc.css