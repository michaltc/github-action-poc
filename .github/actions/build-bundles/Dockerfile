FROM openjdk:11.0.5-stretch

LABEL "com.github.actions.name"="Build bundles"
LABEL "com.github.actions.description"="Validate bundles and generate output files based on them."
LABEL "com.github.actions.icon"="git-commit"
LABEL "com.github.actions.color"="black"

COPY entrypoint.sh /entrypoint.sh

ENTRYPOINT ["sh", "/entrypoint.sh"]
