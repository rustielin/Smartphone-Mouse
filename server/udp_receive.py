import socket

# UDP_IP = "127.0.0.1"
UDP_IP = "10.142.15.19"
UDP_PORT = 5006


sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((UDP_IP, UDP_PORT))

print("LISTENING NOW")

f = open('out.txt', 'w')

while True:

    data, addr = sock.recvfrom(1024) # buffer size is 1024 bytes
    data = data.decode("utf-8")

    f.write("" + data + "\n")

    print("received message:", data, "\n")
    if data == "quit":
        break
        f.close()
