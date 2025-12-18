#!/bin/bash
# CodeDeploy는 동일 경로에 파일이 이미 존재하면 배포를 실패시키기 때문에
# 재배포 시 충돌을 방지하기 위해 기존 JAR 파일을 사전에 삭제
rm -f /home/ubuntu/recipemate/*.jar
