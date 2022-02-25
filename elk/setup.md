# ELK Setup Based on Docker Compose

## 1. 克隆代码

```shell
git clone https://github.com/deviantony/docker-elk
```

## 2. 进入 docker-elk

```shell
cd docker-elk
```

## 3. 编辑 .env 文件

```shell
vim .env
```

使用 `%s/changeme/你想要的密码/g` 改变密码

`:wq` 退出 vim

## 4. 使用 `docker-compose` 启动

```shell
docker-compose up -d
```