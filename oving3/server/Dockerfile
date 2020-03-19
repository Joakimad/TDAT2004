FROM node:10-alpine

WORKDIR /app

COPY ./server.js /app
COPY ./package.json /app
RUN npm install

COPY . /app
CMD ["node", "server.js"]
