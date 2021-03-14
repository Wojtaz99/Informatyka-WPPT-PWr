# Lista 4 KOMPILATOR
# Autor: Wojciech Pakulski (250350)
# Lista zawiera programy w języku Python3 z użyciem pakietu PLY:

lexer.py Analizer leksykalny (generator tokenów)

memory.py Klasa obiektu pamięci używanej przez kompilator

kompilator.py Parser z generatorem kodu - jest to plik główny

## Jak skompilować kod:
### żeby skompilować wszystkie pliki w Linuxie, należy mieć zainstalowany kompilator języka PYTHON w wersji 3 i pakiet PLY:
### Jak zainstalować je na linuxie?
```
sudo apt-get install python3
sudo apt-get install python3-ply
```
### Jak odpalić na linuxie?
```
python3 kompilator.py nazwa_pliku_wejściowego nazwa_pliku_wyjściowego
```