# This is a PAYMENTS service
### responsible for payments handling and storing sufficient status of payments

### How to run locally
 
1. Create .env file and locate it in parent project directory
2. Provide values for:
```env
   DB_HOST=my-postgres-service
   POSTGRES_DB=
   POSTGRES_USER=
   POSTGRES_PASSWORD= 
```
3. Run docker-compose: `docker compose up -d`

4. It will run docker container for database 
    and another docker container with SpringBoot application. 
    Application will be compiled and packaged before image creation. 

3. No need to Run application: `mvn spring-boot:run`, docker-compose will run executable jar.

### For docker debugging: 
```dockerfile
docker compose down
docker compose down -v (with volume)
docker compose up --build
docker compose up -d
docker compose build --no-cache
docker ps
docker ps -a
docker logs payments-payments-service-1
docker logs <container>
```