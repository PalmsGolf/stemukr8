
package com.mathpar.number;



/**
 * <p>Title: ParCA</p>
 * <p>Description: ParCA - parallel computer algebra system</p>
 * <p>Copyright: Copyright (c) ParCA Tambov, 2005</p>
 * <p>Company: ParCA Tambov</p>
 * @author Yuri Valeev
 * @version 0.5
 */

public class PrimeNumbers {

  public static int LastN; //номер последней вычисленной строки в
  //кэше (устанавливается с помощью метода
  //initCache() и увеличивается с
  //помощью calculate(int lastN))
  public static int MaxN; //максимальный номер элемента  в кэше p
  // (устанавливается с помощью метода
  //initCache())
  private static long[] p; //кэш, содержащий  простые элементы
  //вычисляется с помощью метода calculate(int lastN))
  private static long[][] q; //кэш,содержащий "треугольник" обратных
  // q[i][j] это обратный к p[j] по модулю p[i]
  //вычисляется с помощью метода calculate(int lastN))

  private static int N; //номер затребованного простого
  // числа в кэше p,меняется при каждом вызове
  //метода prime(boolean tag)

  public static int n; //счетчик выбранных подходящих  модулей
  //меняется, если при вызове prime(boolean tag)
  //tag=true.
  private static int[] pIndex; //номера выбранных модулей
  private static NumberZ xBI, yBI; //вспомогательные величины
//____________________________________________________________________________
//Конструктор ( )
//_____________________________________________________________________________
  private PrimeNumbers() {
  }
//_____________________________________________________________________________
//Нахождение следующего за plong простого числа (по убывающей). Вероятность
//того, что найденное число простое равна (1 - 1/(2^certainty)).
//_____________________________________________________________________________
  public static long nextprime(long plong, int certainty) {
    NumberZ p = new NumberZ("0", 10);
    do {
      plong -= 2;
      p = p.valueOf(plong);
    } while (!p.isProbablePrime(certainty));
    return plong;
  }




//____________________________________________________________________________
//Метод, в котором инициируются p[] и q[][],а также все необходимые счетчики
//_____________________________________________________________________________
  public static void initCache(int maxN, int lastN) {
    MaxN = maxN; //установим максимальное число строк в кэше
    long MaxPrime_268435455 = 268435455L;
    p = new long[MaxN + 1]; //создаем
    q = new long[MaxN + 1][MaxN + 1]; //массивы кэша

    p[0] = nextprime(MaxPrime_268435455, 10); //вычисляем
    q[0][0] = 0; //первые элементы в обоих массивах.

    LastN = 1; //записываем число вычисленных элементов
    N = 0; //устанавливаем счетчики
    n = 0; //на ноль
    pIndex = new int[MaxN + 1]; //создаем массив номеров

    calculate(lastN); //вычисляем элементы от 1 до lastN с помощью
    //метода calculate()
  }




//______________________________________________________________________________
//Метод обнуляющий счетчики. (Не затрагивает основных массивов p и q)
//______________________________________________________________________________
  public static void reset() {
    N = 0;
    for (int i = 0; i < n; i++) {
      pIndex[i] = 0;
    }
    n = 0;
  }




//_____________________________________________________________________________
//Метод вычисляющий необходимое число элементов, с помощью методов
//nextprime и obr.
//______________________________________________________________________________
  public static void calculate(int lastN) {
    if (lastN > LastN) { //т.е. если  мы хотим вычислить строки, которых нет в кэше
//Находим простые числа, начиная с LastN до lastN.
      if (lastN < MaxN) {
        for (int i = LastN; i <= lastN; i++) {
          p[i] = nextprime(p[i - 1], 10); //10 величина, характеризующая вероятность того,
          //что выбранные р действительно простые
//Вычисляем недостающие обратные элементы.
          for (int j = 0; j < i; j++) {
            q[i][j] = NFunctionZ32.p_Inverse(p[j], p[i]);
          }
        }
//Вычислив все невычисленные строки кэша от 0 до lastN, установим
//новый номер последней вычисленной строки кэша
        LastN = lastN;
      }

    }
  }




//______________________________________________________________________________
//Возвращает следующее по списку простое число,его номер N. При этом
//либо запоминается номер предыдущего простого в массиве pINdex, либо нет,
// в зависимости от значения  tag.
//______________________________________________________________________________
  public static long prime(boolean tag) {
    if (N > LastN) {
      calculate(N + LastN / 10);
    }
    if ( (tag) & (N != 0)) {
      pIndex[n] = N - 1;
      n++;
    }
    N++;
    return p[N - 1];

  }




//______________________________________________________________________________
//Возвращает массив произведений обратных элементов (в том виде, который необходим
//для схемы Ньютона), извлекая номера выбранных ранее простых модулей из массива
//pIndex.
//______________________________________________________________________________
  public static NumberZ[] obr_prime() {
    NumberZ q_resalt[] = new NumberZ[n - 1];
    NumberZ c = new NumberZ("0");
    for (int i = 0; i <= n - 2; i++) {
      q_resalt[i] = q_resalt[i].ONE;
      for (int j = 0; j <= i; j++)
        q_resalt[i] = q_resalt[i].multiply(c.valueOf(q[pIndex[i + 1]][pIndex[j]]));
    }
    return q_resalt;
  }

}
