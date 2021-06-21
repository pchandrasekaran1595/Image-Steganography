import os
import cv2
import re
import random as r
import numpy as np
import matplotlib.pyplot as plt

import utils

class Steganography(object):
    def __init__(self, path=None, name=None, textname="data.txt", seed=None, debug=None):
        self.path = path
        self.name = name
        if self.name[-3:] != "jpg":
            self.name = self.name.replace(self.name[-3:], "jpg")
        self.textname = textname
        self.seed = seed
        self.debug = debug
    
    def get_image(self):
        return cv2.resize(src=cv2.cvtColor(src=cv2.imread(os.path.join(self.path, self.name), cv2.IMREAD_COLOR), code=cv2.COLOR_BGR2RGB), dsize=(utils.WIDTH, utils.HEIGHT), interpolation=cv2.INTER_AREA)
    
    def show_image(self, image=None, title="Image"):
        if image is None:
            image = self.get_image()
        plt.figure(title)
        plt.imshow(image, cmap="gnuplot2")
        plt.axis("off")
        plt.show()
    
    def getlentxt(self):
        file = open(os.path.join(self.path, self.textname), "r")
        length = len(file.read())
        file.close()
        return length
    
    def showtxt(self):
        file = open(os.path.join(self.path, self.textname), "r")
        utils.breaker()
        print("Text File Contents")
        utils.breaker()
        print(file.read())
        utils.breaker()
        file.close()
    
    def generate_indices(self, w, h):
        r.seed(self.seed)
        len = self.getlentxt()
        img_len = w * h
        assert(len < img_len)
        indices = r.sample(range(1, img_len), len)
        return indices
    
    def generate_ascii(self):
        file = open(os.path.join(self.path, self.textname), "r")
        textData = file.read()
        file.close()
        ascii = []
        for c in textData:
            ascii.append(ord(c))
        return ascii
    
    def do_steganography(self, channel="red"):
        image = self.get_image()
        indices = self.generate_indices(image.shape[1], image.shape[0])
        ascii = self.generate_ascii()
        if re.match(r"red", channel, re.IGNORECASE):
            ch_image = image[:, :, 0].reshape(-1).astype("int64")
            ch_image[indices] = ch_image[indices] + ascii
            ch_image[indices], quotients = ch_image[indices] % utils.DIVISOR, ch_image[indices] // utils.DIVISOR
            ch_image = ch_image.reshape(image.shape[0], image.shape[1])
            image[:, :, 0] = ch_image.astype("uint8")
        elif re.match(r"green", channel, re.IGNORECASE):
            ch_image = image[:, :, 1].reshape(-1).astype("int64")
            ch_image[indices] = ch_image[indices] + ascii
            ch_image[indices], quotients = ch_image[indices] % utils.DIVISOR, ch_image[indices] // utils.DIVISOR
            ch_image = ch_image.reshape(image.shape[0], image.shape[1])
            image[:, :, 1] = ch_image.astype("uint8")
        elif re.match(r"blue", channel, re.IGNORECASE):
            ch_image = image[:, :, 2].reshape(-1).astype("int64")
            ch_image[indices] = ch_image[indices] + ascii
            ch_image[indices], quotients = ch_image[indices] % utils.DIVISOR, ch_image[indices] // utils.DIVISOR
            ch_image = ch_image.reshape(image.shape[0], image.shape[1])
            image[:, :, 2] = ch_image.astype("uint8")
        if self.debug:
            return image, quotients
        else:
            np.save(os.path.join(self.path, "Q-{}.npy".format(self.name[:-4])), quotients)
            cv2.imwrite(os.path.join(self.path, "S-{}.png".format(self.name[:-4])), 
                                     cv2.cvtColor(src=image, code=cv2.COLOR_BGR2RGB))
    
    def recover(self, image, quotients=None, channel="red"):
        indices = self.generate_indices(image.shape[1], image.shape[0])
        oimage = self.get_image()
        if re.match(r"red", channel, re.IGNORECASE):
            data = utils.DIVISOR*quotients + image[:, :, 0].reshape(-1)[indices]
            data = data - oimage[:, :, 0].reshape(-1)[indices]
        elif re.match(r"green", channel, re.IGNORECASE):
            data = utils.DIVISOR*quotients + image[:, :, 1].reshape(-1)[indices]
            data = data - oimage[:, :, 1].reshape(-1)[indices]
        elif re.match(r"blue", channel, re.IGNORECASE):
            data = utils.DIVISOR*quotients + image[:, :, 2].reshape(-1)[indices]
            data = data - oimage[:, :, 2].reshape(-1)[indices]
        text_data = []
        for char in data:
            text_data.append(chr(char))
        text_data = "".join(text_data)
        if self.debug:
            return text_data
        else:
            file = open(os.path.join(self.path, "Recovered.txt"), "w", encoding="utf-8")
            file.write(text_data)
            file.close()
