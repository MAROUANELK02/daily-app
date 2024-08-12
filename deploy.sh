#!/bin/bash

# Variables
RESOURCE_GROUP="daily-app-rg"
LOCATION="eastus"
VNET_NAME="daily-app-vnet"
SUBNET_NAME="daily-app-subnet"
MYSQL_CONTAINER_NAME="mysqlcontainer"
BACKEND_CONTAINER_NAME="backendcontainer"
FRONTEND_CONTAINER_NAME="frontendcontainer"
MYSQL_IMAGE="${ACR_NAME}.azurecr.io/mysql:latest"
BACKEND_IMAGE="${ACR_NAME}.azurecr.io/daily-app-backend:latest"
FRONTEND_IMAGE="${ACR_NAME}.azurecr.io/daily-app-frontend:latest"
MYSQL_PASSWORD="FORTIN-my-secret@9630"
MYSQL_DATABASE="daily_app"
JWT_SECRET="cYAr0yTHvx4NWxZzBFIzndSShkh6JL9ZbJa4cImdsO1VADkCJzg2nXKLgywN7E"
JWT_EXPIRATIONS="3600000"
UPLOAD_PATH="/app/src/main/resources/images"
LOGO_PATH="/app/src/main/resources/static/hps.png"
MAIL_HOST="smtp.gmail.com"
MAIL_PORT="587"
MAIL_USERNAME="marouanelk02@gmail.com"
MAIL_PASSWORD="ipzprwexdihebsxd"

# Create resource group
#az group create --name $RESOURCE_GROUP --location $LOCATION

# Create virtual network and subnet
#az network vnet create --resource-group $RESOURCE_GROUP --name $VNET_NAME --address-prefix 10.0.0.0/16 --subnet-name $SUBNET_NAME --subnet-prefix 10.0.0.0/24

# Create MySQL container
az container create --resource-group $RESOURCE_GROUP --name $MYSQL_CONTAINER_NAME --image $MYSQL_IMAGE --cpu 1 --memory 1.5 --vnet $VNET_NAME --subnet $SUBNET_NAME --environment-variables MYSQL_ROOT_PASSWORD=$MYSQL_PASSWORD MYSQL_DATABASE=$MYSQL_DATABASE

# Create Backend container
az container create --resource-group $RESOURCE_GROUP --name $BACKEND_CONTAINER_NAME --image $BACKEND_IMAGE --cpu 1 --memory 1.5 --vnet $VNET_NAME --subnet $SUBNET_NAME --environment-variables MYSQL_HOST=$MYSQL_CONTAINER_NAME MYSQL_PORT=3306 MYSQL_DATABASE=$MYSQL_DATABASE MYSQL_USER=root MYSQL_PASSWORD=$MYSQL_PASSWORD JWT_SECRET=$JWT_SECRET JWT_EXPIRATIONS=$JWT_EXPIRATIONS UPLOAD_PATH=$UPLOAD_PATH LOGO_PATH=$LOGO_PATH MAIL_HOST=$MAIL_HOST MAIL_PORT=$MAIL_PORT MAIL_USERNAME=$MAIL_USERNAME MAIL_PASSWORD=$MAIL_PASSWORD

# Create Frontend container
az container create --resource-group $RESOURCE_GROUP --name $FRONTEND_CONTAINER_NAME --image $FRONTEND_IMAGE --cpu 1 --memory 1.5 --vnet $VNET_NAME --subnet $SUBNET_NAME