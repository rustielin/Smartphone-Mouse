import socket
import struct

UDP_IP = "10.142.14.43"
UDP_PORT = 5006


sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind((UDP_IP, UDP_PORT))

print("LISTENING NOW")

f = open('out.txt', 'w')

while True:

    data, addr = sock.recvfrom(1024) # buffer size is 1024 bytes

    # data is a list of bytes
    # convert this to floats
    # size = len(data)/4
    # time = ''.join(chr(data[i]) for i in range(0, size))
    # x = ''.join(chr(data[i]) for i in range(size, size*2))
    # y = ''.join(chr(data[i]) for i in range(size*2, size*3))
    # z = ''.join(chr(data[i]) for i in range(size*3, size*4))

    # b = ''.join(chr(i) for i in data)
    # print(b)

    data = data.decode("utf-8")

    time = data[data.index('T')+1:data.index('X')]
    x = float(data[data.index('X')+1:data.index('Y')])
    y = float(data[data.index('Y')+1:data.index('Z')])
    z = float(data[data.index('Z')+1:])

    data_str = "T:{}  X:{} Y:{} Z:{}".format(time,x,y,z)

    print("received message:", data_str)



    f.write("" + data + "\n")

    # isolate x, y, z
    # send as array of bytes!!!!!!!!




    if data == "quit":
        f.close()
        break
