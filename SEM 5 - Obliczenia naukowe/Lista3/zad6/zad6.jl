# Autor: Wojciech Pakulski (250350)
include("zera_funkcji.jl")

f(x) = exp(1.0-x) - 1.0
pf(x) = -exp(1-x)
g(x) = x * exp(-x)
pg(x) = -exp(-x) * (x-1)
d = 10.0^(-5)
e = 10.0^(-5)
maxiter = 30

# Metoda bisekcji dla f(x)
println("Metoda bisekcji dla f(x):")
res = zera_funkcji.mbisekcji(f, 0.0, 2.0, d, e)
println("[0.0, 2.0] $res")

res = zera_funkcji.mbisekcji(f, 0.0, 1.5, d, e)
println("[0.0, 1.5] $res")

res = zera_funkcji.mbisekcji(f, -1.5, 100.0, d, e)
println("[-1.5, 100.0] $res")

# Metoda bisekcji dla g(x)
println("\nMetoda bisekcji dla g(x):")
res = zera_funkcji.mbisekcji(g, -0.75, 0.75, d, e)
println("[-0.75, 0.75] $res")

res = zera_funkcji.mbisekcji(g, -0.75, 0.3, d, e)
println("[-0.75, 0.3] $res")

res = zera_funkcji.mbisekcji(g, -41.7, 13.25, d, e)
println("[-41.7, 13.25] $res")

# Metoda Newtona dla f(x)
println("\nMetoda Newtona dla f(x):")
res = zera_funkcji.mstycznych(f, pf, 0.99, d, e, maxiter)
println("0.99 $res")

res = zera_funkcji.mstycznych(f, pf, -2.0, d, e, maxiter)
println("-2.0 $res")

res = zera_funkcji.mstycznych(f, pf, 3.0, d, e, maxiter)
println("3.0 $res")

res = zera_funkcji.mstycznych(f, pf, 9.0, d, e, maxiter)
println("9.0 $res")

# Metoda Newtona dla g(x)
println("\nMetoda Newtona dla g(x):")
res = zera_funkcji.mstycznych(g, pg, -0.01, d, e, maxiter)
println("-0.01 $res")

res = zera_funkcji.mstycznych(g, pg, -1.0, d, e, maxiter)
println("-1.0 $res")

res = zera_funkcji.mstycznych(g, pg, 0.01, d, e, maxiter)
println("0.01 $res")

res = zera_funkcji.mstycznych(g, pg, 1.0, d, e, maxiter)
println("1.0 $res")

res = zera_funkcji.mstycznych(g, pg, 2.0, d, e, maxiter)
println("2.0 $res")

# Metoda siecznych dla f(x)
println("\nMetoda siecznych dla f(x):")

res = zera_funkcji.msiecznych(f, 0.0, 0.5, d, e, maxiter)
println("0.0 0.5 $res")

res = zera_funkcji.msiecznych(f, -10.0, -5.5, d, e, maxiter)
println("-10.0 -5.5 $res")

res = zera_funkcji.msiecznych(f, 1.5, 2.0, d, e, maxiter)
println("1.5 2.0 $res")

# Metoda siecznych dla g(x)
println("\nMetoda siecznych dla g(x):")
res = zera_funkcji.msiecznych(g, -0.1, -0.01, d, e, maxiter)
println("-0.1 -0.01 $res")

res = zera_funkcji.msiecznych(g, -1.0, -0.9, d, e, maxiter)
println("-1.0 -0.9 $res")

res = zera_funkcji.msiecznych(g, -10.0, -6.0, d, e, maxiter)
println("-10.0 -6.0 $res")

res = zera_funkcji.msiecznych(g, -1.0, 0.5, d, e, maxiter)
println("-1.0 0.5 $res")

res = zera_funkcji.msiecznych(g, -1.0, 2.0, d, e, maxiter)
println("-1.0 2.0 $res")

res = zera_funkcji.msiecznych(g, 5.0, 10.0, d, e, maxiter)
println("5.0 10.0 $res")
