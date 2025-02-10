FROM postgres

ENV POSTGRES_USER itmo_user
ENV POSTGRES_PASSWORD pass
ENV POSTGRES_DB inventory

COPY init_db.sql /docker-entrypoint-initdb.d/

EXPOSE 5432