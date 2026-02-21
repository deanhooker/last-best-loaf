.PHONY: up down restart logs ps reset-db nuke help

POD=bakery
DB_CONTAINER=bakery-db
DB_VOLUME=postgres_data

help:
	@echo ""
	@echo "Bakery dev commands:"
	@echo ""
	@echo "make start     Start dev infrastructure"
	@echo "make stop      Stop infrastructure"
	@echo "make restart   Restart infrastructure"
	@echo "make logs      Tail database logs"
	@echo "make ps        Show pod + containers"
	@echo "make reset-db  Destroy and recreate database"
	@echo "make nuke      Destroy EVERYTHING (pod + volumes)"
	@echo ""

start:
	@echo "Starting bakery infra..."

	./podman/pod.sh
	./podman/db.sh

	@echo ""
	@echo "Bakery dev environment ready!"
	@echo ""
	@echo "Postgres:"
	@echo "  host: localhost"
	@echo "  port: 5432"
	@echo "  db: bakery"
	@echo "  user: bakery"
	@echo "  password: bakery"

stop:
	podman pod stop $(POD) || true

restart: down up

logs:
	podman logs -f $(DB_CONTAINER)

ps:
	podman pod ps
	podman ps -a

reset-db:
	podman rm -f $(DB_CONTAINER) || true
	podman volume rm $(DB_VOLUME) || true
	./podman/db.sh

nuke:
	podman pod rm -f $(POD) || true
	podman volume rm $(DB_VOLUME) || true
