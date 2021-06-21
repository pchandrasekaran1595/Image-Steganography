import os
import cv2
import sys
import numpy as np

import utils
from Steganography import Steganography

        
def app():
    args_1 = "--name"
    args_2 = "--seed"

    args_3 = "--red"
    args_4 = "--green"
    args_5 = "--blue"

    args_6 = "--hide"
    args_7 = "--recover"
    args_8 = "--debug"

    name = "car.jpg"
    seed = 0
    channel = "red"
    hide = None
    recover = None
    debug = None

    if args_1 in sys.argv:
        name = sys.argv[sys.argv.index(args_1) + 1]
    if args_2 in sys.argv:
        seed = int(sys.argv[sys.argv.index(args_2) + 1])

    if args_3 in sys.argv:
        channel = "red"
    if args_4 in sys.argv:
        channel = "green"
    if args_5 in sys.argv:
        channel = "blue"
    
    if args_6 in sys.argv:
        hide = True
    if args_7 in sys.argv:
        recover = True
    if args_8 in sys.argv:
        debug = True
    
    S = Steganography(path=utils.PATH, name=name, seed=seed, debug=debug)

    if debug:
        image, quotients = S.do_steganography(channel=channel)
        data = S.recover(image=image, quotients=quotients, channel=channel)
        utils.breaker()
        print(data)
        utils.breaker()
    else:
        if hide:
            S.do_steganography(channel=channel)
        
        if recover:
            image = cv2.cvtColor(src=cv2.imread(os.path.join(utils.PATH, "S-{}.png".format(name[:-4])), cv2.IMREAD_COLOR), code=cv2.COLOR_BGR2RGB)
            S.recover(image=image, quotients=np.load(os.path.join(utils.PATH, "Q-{}.npy".format(name[:-4]))), channel=channel)
