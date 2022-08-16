import sys

# print offset of char in file
def print_file_char_offset(file_name):
    with open(file_name) as file:
        # print(file.read())
        file_content = file.read()
        print_string_char_offset( file_content )

# print char index in string
def print_string_char_offset(string):
    # for i, el in enumerate([c for c in string]):
    #     print(el,f"{i:04}")
    for i in range(0, len(string), 10):
        print_index_next_line(string, i, i + 10)

def print_index_next_line(string : str , start : int, end : int):
    min_end = min(end, len(string))
    for i in range(start, min_end):
        to_print = (f"{string[i]}").replace('\n',r'\n')
        print(f"{to_print: >4}", end="")
    print()
    for i in range(start, min_end):
        print(f" {i:03}", end="")
    print()

if __name__ == "__main__":
    if sys.argv[1] == "-f":
        print_file_char_offset(sys.argv[2])
    else :
        print_string_char_offset(sys.argv[1])
