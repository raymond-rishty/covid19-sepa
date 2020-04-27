sam package --profile rrishty --template-file sam.yaml --s3-bucket lambda-artifacts.rrishty --output-template-file packaged.yaml
sam deploy --profile rrishty --region us-east-1 --template-file ./packaged.yaml --stack-name covid-19-sepa-report --capabilities CAPABILITY_IAM
pandoc web/report.md -o web/report.html
aws s3 cp --profile rrishty --acl public-read web/report.html s3://covid19.data.rrishty/report.html
aws s3 cp --profile rrishty --acl public-read web/pandoc.css s3://covid19.data.rrishty/pandoc.css