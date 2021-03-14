class Memory():
    # Klasa pamięci kompilatora
    def __init__(self):
        self.memory_count = -1
        self.var = {}
        self.tables = {}
        self.inits = {}


    def new_array(self, id, start, end, lineno):
        # Tworzy nową tablicę w pamięci
        if end < start:
            raise Exception("Niewłaściwy zakres tablicy " + id + ", w linii " + lineno)
        pointer = self.memory_count + 1
        self.tables[id] = (pointer, start, end)
        self.memory_count += (end - start + 1)


    def new_var(self, id, lineno):
        # Tworzy nową zmienną w pamięci
        if id in self.var:
            raise Exception("Zmienna " + id + " już istnieje" + ", w linii " + lineno)
        self.memory_count += 1
        self.var[id] = self.memory_count


    def rm_var(self, id):
        # Usuwa zmienną z pamięci
        self.var.pop(id)


    def new_temp_var(self):
        # Tworzy w pamięci zmienną tymczasową 
        temp_var_name = "$t" + str(self.memory_count)
        self.new_var(temp_var_name, None)
        self.inits[temp_var_name] = True
        return temp_var_name


    def get_var_index(self, variable, lineno):
        # Zwraca dane adresowe zmiennej o nazwie variable komórki w pamięci
        if variable not in self.var:
            raise Exception("Podano nieistniejącą zmienną, w linii " + lineno)
        else:
            return self.var[variable]


    def get_tab_data(self, array, lineno):
        # Zwraca dane adresowe tablicy o nazwie variable komórki w pamięci
        if array not in self.tables:
            raise Exception("Żądanie nieistniejącej tablicy, w linii " + lineno)
        else:
            return self.tables[array]


    def init_var(self, variable):
        # Ustawia zmienną na zainicjowaną
        self.inits[variable] = True


    def is_var_init(self, id, lineno):
        # Sprawdza, czy zmienna jest zainicjowana
        if id not in self.inits:
            raise Exception("Zmienna " + str(id) +\
                            " nie została jeszcze zainicjowana" + ", w linii " + str(lineno)) 


    def is_var_address_valid(self, id, lineno):
        if id not in self.var:
            if id in self.tables:
                raise Exception("Złe przyporządkowanie tablicy " + str(id) + ", w linii " + str(lineno))
            else:
                raise Exception("Niezadeklarowana zmienna " + str(id) + ", w linii " + str(lineno))


    def is_array_address_valid(self, id, lineno):
        if id not in self.tables:
            if id in self.var:
                raise Exception("Złe przyporządkowanie zmiennej " + str(id) + ", w linii " + str(lineno)) 
            else:
                raise Exception("Niezadeklarowana tablica " + str(id) + ", w linii " + str(lineno))
            