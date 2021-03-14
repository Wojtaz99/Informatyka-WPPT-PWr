using SparseArrays
include("blocksys.jl")


function main_test()
    sizes = [50000]
    for i in sizes
        test("tests/Dane$i/A.txt", "tests/Dane$i/b.txt", true, true,"wynik1.txt")
        test("tests/Dane$i/A.txt", "tests/Dane$i/b.txt", false, false, "wynik.txt")
        testKomputera("tests/Dane$i/A.txt", "tests/Dane$i/b.txt")
    end
end

"""
Komputer liczy
"""
function testKomputera(matrix::String, vector::String)
    A, n, l = blocksys.wczytaj_macierz_z_pliku(matrix)
    b = blocksys.wczytaj_wektor_prawych_stron_z_pliku(vector)
    @time begin 
        A\b
    end
end

"""
test
"""
function test(matrix::String, vector::String, is_right_generated::Bool, with_main_element::Bool, file::String)
    A, n, l = blocksys.wczytaj_macierz_z_pliku(matrix)
    if is_right_generated
        b = blocksys.generuj_wektor(A, n, l)
    else
        b = blocksys.wczytaj_wektor_prawych_stron_z_pliku(vector)
    end
    if with_main_element
        @time begin 
            x = blocksys.eliminacja_gaussa2(A, b, n, l)
        end
    else
        @time begin 
            x = blocksys.eliminacja_gaussa(A, b, n, l)
        end
    end
    blocksys.zapisz_wyniki_do_pliku(file, x, n, is_right_generated)
end

main_test()
