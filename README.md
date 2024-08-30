# Spring Cloud Stream demo V3

Example using the functional and reactive model. It shows how to send 
messages to a topic using different routing keys.

## Instructions

This example needs a RabbitMQ broker. You can use the one included as part 
of the project, in docker compose file.

Run the docker compose:

    docker-compose up --build

Then just execute the publisher project to provide messages every 3 seconds, and the 
consumer project to receive the messages on the different queues.


