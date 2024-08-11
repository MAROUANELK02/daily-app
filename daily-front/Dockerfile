# Étape 1: Construire l'application Angular
FROM node:alpine AS build

WORKDIR /usr/src/app

COPY . /usr/src/app

RUN npm install -g @angular/cli
RUN npm install
RUN ng build --configuration production

# Étape 2: Servir l'application avec Nginx
FROM nginx:alpine

COPY --from=build /usr/src/app/dist/daily_frontend /usr/share/nginx/html

# Copier le fichier de configuration Nginx personnalisé
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
