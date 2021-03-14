#!/usr/bin/python3
import sys
import ply.yacc as yacc
from lexer import tokens
import re
import lexer
import memory


# Obiekt reprezentujący pamięć wykorzystywaną przez kompilator
mem = memory.Memory()
jumps_and_labels = []


def generate_number(number, to_register):
	# Generuje we wskazanym rejestrze zadeklarowaną liczbę
        list = ""
        while number != 0:
            if number % 2 == 0:
                number = number // 2
                list = "SHL " + to_register + "\n" + list
            else:
                number = number - 1
                list = "INC " + to_register + "\n" + list
        list = "RESET " + to_register + "\n" + list 
        return list


def address_to_register(value, register, lineno):
    if value[0] == "id":
        mem.is_var_address_valid(value[1], lineno)
        return	generate_number(mem.get_var_index(value[1], lineno), register)
    elif value[0] == "tab":
        mem.is_array_address_valid(value[1], lineno)
        tab_pos, tab_start, tab_stop = mem.get_tab_data(value[1], lineno)
        cell_index = value[2]
        return	value_to_register(cell_index, register,  "d", lineno) +\
            	generate_number(tab_start, "b") +\
                "SUB " + register + " b" + "\n" +\
                generate_number(tab_pos, "b") +\
                "ADD " + register + "b" + "\n"
    elif value[0] == "number":
        return value_to_register(value, "a", register, lineno) +\
                address_to_register(("id", mem.new_temp_var()), register, lineno) +\
                "STORE a " + register + "\n"


def value_to_register(value, register, register_with_address, lineno):
    if value[0] == "number":
        return	generate_number(int(value[1]), register)
    elif value[0] == "id":
        mem.is_var_init(value[1], lineno)
    return	"RESET d\n" +\
			address_to_register(value, "d", lineno) +\
            "LOAD " + register + " " + register_with_address + "\n"


# POMOCNICZE FUNKCJE OBLICZAJĄCE SKOKI

def new_labels(count):
	new_labels = []
	new_jumps = []
	for _ in range(0,count):
		jumps_and_labels.append(None)
		number = str(len(jumps_and_labels) - 1)
		new_labels.append("#L" + number + "#")
		new_jumps.append("#J" + number + "#")
	return (new_labels, new_jumps)
	
def replace_jumps(generated_code):
	line_num = 0
	removed_labels = []
	for line in generated_code.split("\n"):
		match = re.search("#L[0-9]+#", line)
		if match:
			label_id = int(match.group()[2:-1])
			jumps_and_labels[label_id] = line_num
			line = re.sub("#L[0-9]+#", "", line)
		removed_labels.append(line)
		line_num += 1
	
	removed_jumps = ""
	for i, line in enumerate(removed_labels):
		match = re.search("#J[0-9]+#", line)
		if match:
			jump_id = int(match.group()[2:-1])
			jump_line = jumps_and_labels[jump_id] - i
			line = re.sub("#J[0-9]+#", str(jump_line), line)
		removed_jumps += line + "\n"
	return removed_jumps


# IMPLEMENTACJA GRAMATYKI
	
def p_program_declare(p):
	'''generated_code : DECLARE declarations BEGIN commands END'''
	p[0] = replace_jumps(p[4] + "HALT")


def p_program(p):
	'''generated_code : BEGIN commands END'''
	p[0] = replace_jumps(p[2] + "HALT")	


def p_declarations_variable(p):
	'''declarations	: declarations COMMA ID'''
	p[0] = p[1]
	id, lineno = p[3], str(p.lineno(2))	
	mem.new_var(id, lineno)
	

def p_declarations_array(p):
	'''declarations	: declarations COMMA ID LPAREN NUMBER COLON NUMBER RPAREN'''
	p[0] = p[1]
	id, start, end, lineno = p[3], p[5], p[7], str(p.lineno(2))
	mem.new_array(id, int(start), int(end), lineno)	


def p_id(p):
	'''declarations	: ID'''
	id, lineno = p[1], str(p.lineno(1))	
	mem.new_var(id, lineno)


def p_array(p):
	'''declarations	: ID LPAREN NUMBER COLON NUMBER RPAREN'''
	id, start, end, lineno = p[1], p[3], p[5], str(p.lineno(2))
	mem.new_array(id, int(start), int(end), lineno)


def p_commands_mult(p):
	'''commands	: commands command'''
	p[0] =	p[1] + p[2]


def p_commands_one(p):
	'''commands	: command'''
	p[0] = p[1]
				

def p_command_assign(p):
	'''command	: identifier ASSIGN expression SEMICOLON'''
	identifier, expression, lineno = p[1], p[3], str(p.lineno(1)) 
	p[0] =	expression +\
			address_to_register(identifier, "d", lineno) +\
			"STORE a d\n"
	mem.init_var(identifier[1])


def p_command_if(p):
	'''command	: IF condition THEN commands ENDIF'''
	condition, commands_if = p[2], p[4]
	p[0] =	condition[0] +\
			commands_if +\
			condition[1]


def p_command_if_else(p):
	'''command	: IF condition THEN commands ELSE commands ENDIF'''			
	condition, commands_if, commands_else = p[2], p[4], p[6]
	jumps_labels, jumps_marker = new_labels(1)
	p[0] =	condition[0] +\
			commands_if +\
			"JUMP " + jumps_marker[0] + "\n" +\
			condition[1] +\
			commands_else +\
			jumps_labels[0]


def p_command_while(p):
	'''command	: WHILE condition DO commands ENDWHILE'''
	condition, commands = p[2], p[4]
	jumps_labels, jumps_marker = new_labels(1)
	
	p[0] =	jumps_labels[0] +\
			condition[0] +\
			commands +\
			"JUMP " + jumps_marker[0] + "\n" +\
			condition[1]


def p_command_repeatuntil(p):
	'''command	: REPEAT commands UNTIL condition SEMICOLON'''
	commands, condition = p[2], p[4]
	jumps_labels, jumps_marker = new_labels(1)
	
	p[0] =	jumps_labels[0] +\
			commands +\
			condition[0] +\
			"JUMP 2" +\
			"JUMP " + jumps_marker[0] + "\n" +\
			condition[1]


def p_iterator(p):
	'''iterator	: ID'''	
	id, lineno = p[1], str(p.lineno(1)) 
	p[0] = id
	mem.new_var(id, lineno)
	mem.init_var(id)


def p_command_for_to(p):
	'''command	: FOR iterator FROM value TO value DO commands ENDFOR'''
	iterator, start_val, stop_val, commands, lineno = p[2], p[4], p[6], p[8], str(p.lineno(1))
	is_doubled_var_in_loop(iterator, start_val, stop_val)
	jumps_labels, jumps_marker = new_labels(2)
	temp_var = mem.new_temp_var()
		
	p[0] =	value_to_register(stop_val, "e", "d", lineno) +\
			address_to_register(("id", temp_var), "d", lineno) +\
			"RESET a\n" +\
			"STORE e d\n" +\
			value_to_register(start_val, "f", "d", lineno) +\
			address_to_register(("id", iterator), "d", lineno) +\
			"STORE f d\n" +\
			jumps_labels[1] +\
			value_to_register(("id", temp_var), "e", "d", lineno) +\
			value_to_register(("id", iterator), "f", "d", lineno) +\
			"SUB f e\n" +\
			"JZERO f 2\n" +\
			"JUMP " + jumps_marker[0] + "\n" +\
			commands +\
			value_to_register(("id", iterator), "f", "d", lineno) +\
			"INC f\n" +\
			address_to_register(("id", iterator), "d", lineno) +\
			"STORE f d\n" +\
			"JUMP " + jumps_marker[1] + "\n" +\
			jumps_labels[0]
	mem.rm_var(iterator)


def p_command_for_downto(p):
	'''command	: FOR iterator FROM value DOWNTO value DO commands ENDFOR'''
	iterator, start_val, stop_val, commands, lineno = p[2], p[4], p[6], p[8], str(p.lineno(1)) 
	jumps_labels, jumps_marker = new_labels(2)
	is_doubled_var_in_loop(iterator, start_val, stop_val)
	temp_var = mem.new_temp_var()

	p[0] = value_to_register(stop_val, "e", "d", lineno) +\
			address_to_register(("id", temp_var), "d", lineno) +\
			"RESET a\n" +\
			"STORE e d\n" +\
			value_to_register(start_val, "f", "d", lineno) +\
			address_to_register(("id", iterator), "d", lineno) +\
			"STORE f d\n" +\
			jumps_labels[1] +\
			value_to_register(("id", temp_var), "e", "d", lineno) +\
			value_to_register(("id", iterator), "f", "d", lineno) +\
			"SUB e f\n" +\
			"JZERO e 2\n" +\
			"JUMP " + jumps_marker[0] + "\n" +\
			commands +\
			value_to_register(("id", iterator), "f", "d", lineno) +\
			address_to_register(("id", iterator), "d", lineno) +\
			"JZERO f 4\n" +\
			"DEC f\n" +\
			"STORE f d\n" +\
			"JUMP " + jumps_marker[1] + "\n" +\
			jumps_labels[0]
	mem.rm_var(iterator)

	
# INPUT i OUTPUT

def p_command_read(p):
	'''command	: READ identifier SEMICOLON'''
	identifier, lineno = p[2], str(p.lineno(1)) 
	mem.inits[identifier[1]] = True
	p[0] = address_to_register(identifier, "d", lineno) +\
			"GET d\n"


def p_command_write(p):
	'''command	: WRITE value SEMICOLON'''	
	value, lineno = p[2], str(p.lineno(1))
	if value[0] == "id":
		mem.is_var_init(value[1], lineno)
	p[0] = address_to_register(value, "d", lineno) +\
			"PUT d\n"


# DZIAŁANIA ARYTMETYCZNE

def p_expression_value(p):
	'''expression : value'''
	value, lineno = p[1], str(p.lineno(1))
	p[0] = value_to_register(value, "a", "d", lineno)


def p_expression_plus(p):
	'''expression : value PLUS value'''
	value1, value2, lineno = p[1], p[3], str(p.lineno(1))
	p[0] =	value_to_register(value1, "a", "d", lineno) +\
			value_to_register(value2, "b", "d", lineno) +\
			"ADD a b\n"


def p_expression_minus(p):
	'''expression : value MINUS value'''
	value1, value2, lineno = p[1], p[3], str(p.lineno(1))
	p[0] = value_to_register(value1, "a", "d", lineno) +\
			value_to_register(value2, "b", "d", lineno) +\
			"SUB a b\n"


def p_expression_times(p):
	'''expression : value TIMES value'''
	value1, value2, lineno = p[1], p[3], str(p.lineno(1)) 	
	p[0] =  value_to_register(value1, "a", "d", lineno) +\
			value_to_register(value2, "b", "d", lineno) +\
			"RESET c\n" +\
			"JZERO a 7\n" +\
			"JODD a 2\n" +\
			"JUMP 2\n" +\
			"ADD c b\n" +\
			"SHR a\n" +\
			"SHL b\n" +\
			"JUMP -6\n" +\
			"RESET a\n" +\
			"ADD a c\n"


def p_expression_divide(p):
	'''expression : value DIVIDE value'''
	value1, value2, lineno = p[1], p[3], str(p.lineno(1))
	p[0] =  value_to_register(value1, "a", "d", lineno) +\
			value_to_register(value2, "b", "d", lineno) +\
			"JZERO b 24\n" +\
			"RESET c\n" +\
			"INC c\n" +\
			"RESET d\n" +\
			"ADD d a\n" +\
			"SUB d b\n" +\
			"JZERO d 4\n" +\
			"SHL b\n" +\
			"SHL c\n" +\
			"JUMP -6\n" +\
			"RESET e\n" +\
			"ADD e a\n" +\
			"RESET a\n" +\
			"RESET d\n" +\
			"ADD d b\n" +\
			"SUB d e\n" +\
			"JZERO d 2\n" +\
			"JUMP 3\n" +\
			"SUB e b\n" +\
			"ADD a c\n" +\
			"SHR b\n" +\
			"SHR c\n" +\
			"JZERO c 3\n" +\
			"JUMP -10\n" +\
			"RESET a\n"


def p_expression_modulo(p):
	'''expression : value MODULO value'''
	value1, value2, lineno = p[1], p[3], str(p.lineno(1))
	p[0] =  value_to_register(value1, "a", "d", lineno) +\
			value_to_register(value2, "b", "d", lineno) +\
			"JZERO b 24\n" +\
			"RESET c\n" +\
			"INC c\n" +\
			"RESET d\n" +\
			"ADD d a\n" +\
			"SUB d b\n" +\
			"JZERO d 4\n" +\
			"SHL b\n" +\
			"SHL c\n" +\
			"JUMP -6\n" +\
			"RESET e\n" +\
			"ADD e a\n" +\
			"RESET a\n" +\
			"RESET d\n" +\
			"ADD d b\n" +\
			"SUB d e\n" +\
			"JZERO d 2\n" +\
			"JUMP 3\n" +\
			"SUB e b\n" +\
			"ADD a c\n" +\
			"SHR b\n" +\
			"SHR c\n" +\
			"JZERO c 2\n" +\
			"JUMP -10\n" +\
			"RESET a\n" +\
			"ADD a e\n" 


def p_condition_equals(p):
	'''condition	: value EQ value'''
	value1, value2, lineno = p[1], p[3], str(p.lineno(1))
	jumps_labels, jumps_marker = new_labels(1)
	p[0] = (value_to_register(value1, "a", "d", lineno) +\
			value_to_register(value2, "b", "d", lineno) +\
			"RESET c\n" +\
			"ADD c a\n" +\
			"SUB c b\n" +\
			"JZERO c 2\n" +\
			"JUMP " + jumps_marker[0] + "\n" +\
			"RESET c\n" +\
			"ADD c b\n" +\
			"SUB c a\n" +\
			"JZERO c 2\n" +\
			"JUMP " + jumps_marker[0] + "\n",
			jumps_labels[0])


def p_condition_not_equals(p):
	'''condition	: value NEQ value'''
	value1, value2, lineno = p[1], p[3], str(p.lineno(1))
	jumps_labels, jumps_marker = new_labels(1)
	p[0] = (value_to_register(value1, "a", "d", lineno) +\
			value_to_register(value2, "b", "d", lineno) +\
			"RESET c\n" +\
			"ADD c a\n" +\
			"SUB c b\n" +\
			"JZERO c 2\n" +\
			"JUMP 5\n" +\
			"RESET c\n" +\
			"ADD c b\n" +\
			"SUB c a\n" +\
			"JZERO c " + jumps_marker[0] + "\n",
			jumps_labels[0])


def p_condition_less(p):
	'''condition	: value LESS value'''
	value1, value2, lineno = p[1], p[3], str(p.lineno(1)) 	
	jumps_labels, jumps_marker = new_labels(1)

	p[0] = (value_to_register(value1, "a", "d", lineno) +\
			value_to_register(value2, "b", "d", lineno) +\
			"RESET c\n" +\
			"ADD c b\n" +\
			"SUB c a\n" +\
			"JZERO c " + jumps_marker[0] + "\n",
			jumps_labels[0])	


def p_condition_more(p):
	'''condition	: value MORE value'''
	value1, value2, lineno = p[1], p[3], str(p.lineno(1)) 	
	jumps_labels, jumps_marker = new_labels(1)

	p[0] = (value_to_register(value2, "a", "d", lineno) +\
			value_to_register(value1, "b", "d", lineno) +\
			"RESET c\n" +\
			"ADD c b\n" +\
			"SUB c a\n" +\
			"JZERO c " + jumps_marker[0] + "\n",
			jumps_labels[0])	


def p_condition_less_equals(p):
	'''condition	: value LEQ value'''
	value1, value2, lineno = p[1], p[3], str(p.lineno(1)) 	
	jumps_labels, jumps_marker = new_labels(1)

	p[0] = (value_to_register(value2, "a", "d", lineno) +\
			value_to_register(value1, "b", "d", lineno) +\
			"RESET c\n" +\
			"ADD c b\n" +\
			"SUB c a\n" +\
			"JZERO c 2\n" +\
			"JUMP " + jumps_marker[0] + "\n",
			jumps_labels[0])


def p_condition_more_equals(p):
	'''condition	: value MEQ value'''
	value1, value2, lineno = p[1], p[3], str(p.lineno(1)) 	
	jumps_labels, jumps_marker = new_labels(1)

	p[0] = (value_to_register(value1, "a", "d", lineno) +\
			value_to_register(value2, "b", "d", lineno) +\
			"RESET c\n" +\
			"ADD c b\n" +\
			"SUB c a\n" +\
			"JZERO c 2\n" +\
			"JUMP " + jumps_marker[0] + "\n",
			jumps_labels[0])
	

def p_value_number(p): 
	'''value : NUMBER'''
	p[0] = ("number", p[1])


def p_value_identifier(p):
	'''value : identifier'''
	p[0] = p[1]


def p_identifier_id(p):
	'''identifier	: ID'''
	p[0] = ("id", p[1])


def p_identifier_tab_id(p):
	'''identifier	: ID LPAREN ID RPAREN'''
	p[0] = ("tab", p[1], ("id", p[3]))


def p_identifier(p):
	'''identifier	: ID LPAREN NUMBER RPAREN'''
	p[0] = ("tab", p[1], ("number", p[3]))


def p_error(p):
	# Obsługa wystąpienia błędnego znaku
	raise Exception("Błędny znak " + str(p.value) + ", w linii " + str(p.lineno)) 


def is_doubled_var_in_loop(iterator, start_val, stop_val):
	if iterator == start_val[1] or iterator == stop_val[1]:
		raise Exception("Użycie niezadeklarowanej zmiennej " + iterator +\
			 			" o tej samej nazwie co iterator w zakresie pętli")


sys.argv.append("gebali/8-for.imp")
sys.argv.append("out")
# MAIN
if __name__ == "__main__":
	try:
		l = lexer.get_lexer()
		parser = yacc.yacc()
		f = open(sys.argv[1], "r")
		parsed = parser.parse(f.read(),tracking=True)
		with open(sys.argv[2], "w") as file:
			file.write(parsed)
	except IndexError:
		print("Błędne wywołanie! Spróbuj:\n" +\
			"python3 kompilator.py nazwa_pliku_wejściowego nazwa_pliku_wyjściowego")
	except Exception as e:
		print(e)
	finally:
		exit()
