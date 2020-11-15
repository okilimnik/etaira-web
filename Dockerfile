FROM node:12
WORKDIR /usr/src/app
COPY package.json ./
RUN yarn install
COPY . .

FROM clojure:tools-deps
WORKDIR /usr/src/app
COPY shadow-cljs.edn ./
COPY deps.edn ./
RUN clojure -P
RUN clojure -M:shadow-cljs classpath
COPY --from=0 /usr/src/app/node_modules node_modules/
COPY . .
RUN clojure -M:shadow-cljs release app
RUN clojure -e "(compile 'com.example.components.server)"
RUN clojure -M:app --aliases package --main-class com.example.components.server --level debug
EXPOSE 8080
CMD ["java", "-jar", "target/app.jar"]

#FROM openjdk:11.0.9.1-jre
#COPY . .
#EXPOSE 8080
#CMD ["java", "-jar", "target/workspace.jar"]