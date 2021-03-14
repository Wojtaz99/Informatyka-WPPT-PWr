# Autor: Wojciech Pakulski (250350)
"""
Funkcja porównująca epsilon maszynowy komputera, 
a tym wyliczonym przez Kahana
# Arguments
- `type`: typ zmienych dla jakich chcemy liczyć macheps
"""
function kahan(type)
	println("Dla ", type, " eps Kahana: ", type(3 * type((type(4/3) - 1)) - 1), " eps maszynowy: ", eps(type))
end

# Wyliczamy eps Kahana dla każdego z typów
for i = [Float16, Float32, Float64]
	kahan(i)
end
