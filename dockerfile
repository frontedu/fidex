FROM nixos/nix

RUN nix-env -iA nixpkgs.nixpkgs-nixpkgs-unstable

ENV NIXPACKS /nixpacks
RUN mkdir -p $NIXPACKS
WORKDIR $NIXPACKS

COPY nixpacks.nix .

RUN nixpkgs-unstable.nix-build

ENV MAVEN_HOME /nixpacks/.nix-profile/share/maven
ENV PATH $MAVEN_HOME/bin:$PATH
ENV JAVA_HOME /nixpacks/.nix-profile/share/jdk

COPY src ./src

RUN mvn clean package

EXPOSE 8080

# Start the Spring Boot application
CMD ["java", "-jar", "target/*.jar"]