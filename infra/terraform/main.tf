terraform {
  required_version = ">= 1.6.0"

  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0"
    }
  }
}

provider "docker" {}

variable "mongo_port" {
  type    = number
  default = 27017
}

resource "docker_network" "franchise_network" {
  name = "franchise-network"
}

resource "docker_volume" "mongo_data" {
  name = "franchise-mongo-data"
}

resource "docker_image" "mongo" {
  name         = "mongo:7.0"
  keep_locally = true
}

resource "docker_container" "mongo" {
  name  = "franchise-mongodb"
  image = docker_image.mongo.image_id

  ports {
    internal = 27017
    external = var.mongo_port
  }

  volumes {
    volume_name    = docker_volume.mongo_data.name
    container_path = "/data/db"
  }

  networks_advanced {
    name = docker_network.franchise_network.name
  }

  restart = "unless-stopped"
}

output "mongodb_uri" {
  value = "mongodb://localhost:${var.mongo_port}/franchise_db"
}