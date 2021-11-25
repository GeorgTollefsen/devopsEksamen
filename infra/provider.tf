terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "3.56.0"
    }
  }
  backend "s3" {
    bucket = "pgr301-geto002-terraform"
    key = "terraform.tfstate"
    region = "eu-west-1"
  }
}
provider "aws" {
  region = "eu-west-1"
}
resource "aws_ecr_repository" "ecr" {
  name = "ecr_geto002_eksamen"
}