# 백그라운드 실행, 강제 재실행
db-start:
	docker-compose -f docker-compose-db.yaml up -d --force-recreate
# volume 삭제
db-shutdown:
	docker-compose -f docker-compose-db.yaml down -v
