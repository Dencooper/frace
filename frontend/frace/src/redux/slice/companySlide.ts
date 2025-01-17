import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { callFetchCompany } from '@/config/api';
import { ICompany } from '@/types/backend';

interface IState {
    isFetching: boolean;
    meta: {
        page: number;
        pageSize: number;
        pages: number;
        total: number;
    },
    result: ICompany[]
}
export const fetchCompany = createAsyncThunk(
    'company/fetchCompany',
    async ({ query }: { query: string }) => {
        const response = await callFetchCompany(query);
        return response;
    }
)


const initialState: IState = {
    isFetching: true,
    meta: {
        page: 1,
        pageSize: 10,
        pages: 0,
        total: 0
    },
    result: []
};


export const companySlide = createSlice({
    name: 'company',
    initialState,
    reducers: {
        setActiveMenu: (state, action) => {
        },


    },
    extraReducers: (builder) => {
        builder.addCase(fetchCompany.pending, (state, action) => {
            state.isFetching = true;
        })

        builder.addCase(fetchCompany.rejected, (state, action) => {
            state.isFetching = false;
        })

        builder.addCase(fetchCompany.fulfilled, (state, action) => {
            if (action.payload && action.payload.data) {
                state.isFetching = false;
                state.meta = action.payload.data.meta;
                state.result = action.payload.data.result;
            }

        })
    },

});

export const {
    setActiveMenu,
} = companySlide.actions;

export default companySlide.reducer;
