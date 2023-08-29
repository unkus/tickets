FROM postgres:latest
ENV POSTGRES_DB ticketSales
ENV POSTGRES_USER ticketSeller
ENV POSTGRES_PASSWORD mypass

COPY initDB.sql /docker-entrypoint-initdb.d/
COPY populateDB.sql /docker-entrypoint-initdb.d/

EXPOSE 5432