FROM openjdk:17-alpine

# don't forget to set final name project on pom.xml
ENV APPLICATION_NAME=chatbot-0.0.1-SNAPSHOT
ARG application_file_name=${APPLICATION_NAME}.jar


USER root
#-------------------------------------------------------------
# Prepare file and application
#-------------------------------------------------------------

ADD docker-cmd.sh /apps/
ADD target/$application_file_name /apps/

#-------------------------------------------------------------
# Set Permission in OS
#-------------------------------------------------------------
RUN cd /apps && chmod +rx $application_file_name && chmod +rx docker-cmd.sh
RUN chown -R 3000:3000 /apps
USER 3000

#-------------------------------------------------------------
# Set default working dir
#-------------------------------------------------------------
WORKDIR  /apps/
EXPOSE 8080

CMD ./docker-cmd.sh

