import os

WIDTH, HEIGHT = 1920, 1080
DIVISOR = 255

# PATH = os.path.join(os.path.abspath(os.path.dirname(__file__)), "Data")
PATH = os.path.join(os.getcwd(), "Data")

def breaker(num=50, char="*"):
    print("\n" + num*char + "\n")

def head(x, no_of_ele=5):
    print(x[:no_of_ele])
