FROM ubuntu:20.04


RUN apt-get update
RUN apt-get -y install curl

RUN curl https://install.citusdata.com/community/deb.sh | sh
RUN apt-get -y install postgresql-17-citus-13.0
RUN pg_conftool 17 main set shared_preload_libraries citus
RUN pg_conftool 17 main set listen_addresses '*'
COPY pg_hba.conf /etc/postgresql/17/main/
USER postgres

CMD ["postgres", "-D /var/lib/postgresql/17/main"]