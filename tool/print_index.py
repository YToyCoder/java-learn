import sys

if __name__ == "__main__":
    for i, el in enumerate([c for c in sys.argv[1]]):
        print(el ,i)
