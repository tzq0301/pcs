# Nacos Configuration

- ip - 101.35.240.47

## Network

name - pcsnet

## Nacos

- id - f806c8d7b1266de0acbcfef3147fabffb52e7d3608d7aee85d9b345abcde5089
- image - nacos/nacos-server
- name - pcs-nacos
- MODE - standalone
- port - 12002
- network - pcsnet
- network-alias - nacos

## Nacos MySQL

- id - 7e8430c18d4f1bdf042bdec67cee409f7c10da5a4ba2426775d798c7cf965b41
- image - mysql
- name - pcs-nacos-mysql
- password - 123456
- port - 12001
- network - pcsnet
- network-alias - mysql

