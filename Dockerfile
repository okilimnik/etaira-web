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
RUN clojure -M:cljs classpath
COPY --from=0 /usr/src/app/node_modules node_modules/
COPY . .
RUN clojure -M:cljs release app
RUN clojure -e "(compile 'etaira.components.server)"
RUN clojure -M:app --aliases package --main-class etaira.components.server --level debug

FROM openjdk:11.0.9.1-jre
COPY --from=1 /usr/src/app/target .
EXPOSE 3000
CMD ["java", "-jar", "app.jar"]