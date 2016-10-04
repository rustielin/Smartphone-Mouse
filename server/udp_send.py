import socket

UDP_IP = "10.142.15.19"

UDP_PORT = 5006

MESSAGE = input("Message? ")



print("UDP target IP:", UDP_IP)
print("UDP target port:", UDP_PORT)
print("message:", MESSAGE)

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # UDP

sock.sendto(MESSAGE.encode('utf-8'), (UDP_IP, UDP_PORT))
